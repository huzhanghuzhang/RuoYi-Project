<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="项目" prop="projectId">
        <el-select
          v-model="queryParams.projectId"
          placeholder="请选择项目"
          clearable
          @change="handleProjectChange"
          style="width: 240px;"
        >
          <el-option
            v-for="item in projectOptions"
            :key="item.projectId"
            :label="item.projectName"
            :value="item.projectId"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="模块" prop="moduleId">
        <el-select
          v-model="queryParams.moduleId"
          placeholder="请选择模块"
          clearable
          style="width: 240px;"
        >
          <el-option
            v-for="item in moduleOptions"
            :key="item.moduleId"
            :label="item.moduleName"
            :value="item.moduleId"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="功能名称" prop="featureName">
        <el-input
          v-model="queryParams.featureName"
          placeholder="请输入功能名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="功能标识" prop="featureKey">
        <el-input
          v-model="queryParams.featureKey"
          placeholder="请输入功能标识(菜单路由)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['project:features:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['project:features:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['project:features:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['project:features:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="featuresList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="featureId" width="50"/>
      <el-table-column label="代码生成表" align="center" prop="tableName" width="120" />
      <el-table-column label="项目" align="center" prop="projectName" width="120" />
      <el-table-column label="模块" align="center" prop="moduleName" width="120" />
      <el-table-column label="功能排序" align="center" prop="featureSort" width="120" />
      <el-table-column label="功能名称" align="center" prop="featureName" width="180" />
      <el-table-column label="功能图标" align="center" prop="featureIcon" width="120" />
      <el-table-column label="功能标识" align="center" prop="featureKey" width="120" />
      <el-table-column label="功能描述" align="center" prop="featureDescription" width=200 />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['project:features:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-setting"
            @click="handleConfigDetail(scope.row)"
            v-hasPermi="['project:features:config']"
          >配置详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['project:features:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改项目模块功能对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item v-if="form.featureId" label="代码生成表ID" prop="tableId">
          <el-input v-model="form.tableId" placeholder="请输入代码生成表ID" :disabled="true" />
        </el-form-item>
        <el-form-item label="项目" prop="projectId">
          <el-select
            v-model="form.projectId"
            placeholder="请选择项目"
            @change="handleDialogProjectChange"
            style="width: 100%;"
          >
            <el-option
              v-for="item in projectOptions"
              :key="item.projectId"
              :label="item.projectName"
              :value="item.projectId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="模块" prop="moduleId">
          <el-select
            v-model="form.moduleId"
            placeholder="请选择模块"
            style="width: 100%;"
            :disabled="!form.projectId"
          >
            <el-option
              v-for="item in moduleOptions"
              :key="item.moduleId"
              :label="item.moduleName"
              :value="item.moduleId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="功能排序" prop="featureSort">
          <el-input v-model="form.featureSort" placeholder="请输入功能排序" />
        </el-form-item>
        <el-form-item label="功能名称" prop="featureName">
          <el-input v-model="form.featureName" placeholder="请输入功能名称" />
        </el-form-item>
        <el-form-item label="功能图标" prop="featureIcon">
          <el-popover
            placement="bottom-start"
            width="460"
            trigger="click"
            @show="$refs['iconSelect'].reset()"
          >
            <IconSelect ref="iconSelect" @selected="selected" :active-icon="form.featureIcon" />
            <el-input slot="reference" v-model="form.featureIcon" placeholder="点击选择图标" readonly>
              <svg-icon
                v-if="form.featureIcon"
                slot="prefix"
                :icon-class="form.featureIcon"
                style="width: 25px;"
              />
              <i v-else slot="prefix" class="el-icon-search el-input__icon" />
            </el-input>
          </el-popover>
        </el-form-item>
        <el-form-item label="功能标识" prop="featureKey">
          <el-input v-model="form.featureKey" placeholder="请输入功能标识(英文)" />
        </el-form-item>
        <el-form-item label="功能描述" prop="featureDescription">
          <el-input v-model="form.featureDescription" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listFeatures, getFeatures, delFeatures, addFeatures, updateFeatures } from "@/api/project/features"
import { listProject } from "@/api/project/project"
import { listModels } from "@/api/project/models"
import IconSelect from "@/components/IconSelect"

export default {
  name: "Features",
  components: { IconSelect },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 项目模块功能表格数据
      featuresList: [],
      // 项目选项
      projectOptions: [],
      // 模块选项
      moduleOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tableId: null,
        projectId: null,
        moduleId: null,
        featureSort: null,
        featureName: null,
        featureIcon: null,
        featureKey: null,
        featureDescription: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        tableId: [
          { required: true, message: "代码生成表ID不能为空", trigger: "blur" }
        ],
        projectId: [
          { required: true, message: "项目ID不能为空", trigger: "blur" }
        ],
        moduleId: [
          { required: true, message: "模块ID不能为空", trigger: "blur" }
        ],
        featureSort: [
          { required: true, message: "功能排序不能为空", trigger: "blur" }
        ],
        featureName: [
          { required: true, message: "功能名称不能为空", trigger: "blur" }
        ],
        featureIcon: [
          { required: true, message: "功能图标不能为空", trigger: "blur" }
        ],
        featureKey: [
          { required: true, message: "功能标识(菜单路由)不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
    this.getProjectList()
    this.getModuleList() // 初始化时加载所有模块
  },
  methods: {
    // 选择图标
    selected(name) {
      this.form.featureIcon = name
    },
    /** 查询项目列表 */
    getProjectList() {
      listProject().then(response => {
        this.projectOptions = response.rows;
      })
    },
    /** 查询模块列表 */
    getModuleList(projectId = null) {
      let query = {};
      if (projectId) {
        query.projectId = projectId;
      }
      listModels(query).then(response => {
        this.moduleOptions = response.rows;
      })
    },
    /** 处理查询表单中项目选择变化 */
    handleProjectChange(val) {
      if (!val) {
        // 如果清空项目选择，则清空模块选择并加载所有模块
        this.queryParams.moduleId = null;
        this.getModuleList();
      } else {
        // 根据所选项目获取对应模块
        this.getModuleList(val);
      }
    },
    /** 处理对话框中项目选择变化 */
    handleDialogProjectChange(val) {
      if (!val) {
        // 如果清空项目选择，则清空模块选择并加载所有模块
        this.form.moduleId = null;
        this.getModuleList();
      } else {
        // 根据所选项目获取对应模块
        this.getModuleList(val);
      }
    },
    /** 查询项目模块功能列表 */
    getList() {
      this.loading = true
      listFeatures(this.queryParams).then(response => {
        this.featuresList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        featureId: null,
        tableId: 0,
        projectId: null,
        moduleId: null,
        featureSort: null,
        featureName: null,
        featureIcon: null,
        featureKey: null,
        featureDescription: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.featureId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加项目模块功能"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const featureId = row.featureId || this.ids
      getFeatures(featureId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改项目模块功能"
        // 先加载项目列表，然后根据当前项目的ID加载对应的模块列表
        this.getProjectList();
        this.$nextTick(() => {
          if (this.form.projectId) {
            this.getModuleList(this.form.projectId);
          }
        });
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.featureId != null) {
            updateFeatures(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addFeatures(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const featureIds = row.featureId || this.ids
      this.$modal.confirm('是否确认删除项目模块功能编号为"' + featureIds + '"的数据项？').then(function() {
        return delFeatures(featureIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 配置详情按钮操作 */
    handleConfigDetail(row) {
      const tableId = row.tableId;
      const tableName = row.featureName;
      const params = { pageNum: this.queryParams.pageNum }
      this.$tab.openPage("修改[" + tableName + "]生成配置", '/tool/gen-edit/index/' + tableId, params)
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('project/features/export', {
        ...this.queryParams
      }, `features_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
